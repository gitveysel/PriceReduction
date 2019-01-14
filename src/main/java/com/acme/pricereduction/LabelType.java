package com.acme.pricereduction;

import java.text.MessageFormat;

public enum LabelType {
	ShowWasNow {
		@Override
		String format(double was, double now, String then1, String then2,String currency) {
			String format = "Was {0}, now {1}";
			return MessageFormat.format(format, PriceFormatter.format(was, currency), PriceFormatter.format(now, currency));

		}
	},
	ShowWasThenNow {
		@Override
		String format(double was, double now, String then1, String then2,String currency) {
			String format = "Was {0}, now {1}";
			String then = "";
			if (then2 != null && !then2.isEmpty())
				then = then2;
			else if (then1 != null && !then1.isEmpty())
				then = then1;

			if (then.isEmpty())
				return MessageFormat.format(format, PriceFormatter.format(was, currency), PriceFormatter.format(now, currency));
			else
				return MessageFormat.format("Was {0}, then {1}, now {2}", PriceFormatter.format(was, currency),
						PriceFormatter.format(Double.parseDouble(then), currency), PriceFormatter.format(now, currency));
		}
	},
	ShowPercDscount {
		@Override
		String format(double was, double now, String then1, String then2,String currency) {
			// TODO Auto-generated method stub
			String format = "{0}% off - now {1}";
			return MessageFormat.format(format, PriceFormatter.format(100.0 * (was - now) / was , currency),
					PriceFormatter.format(now, currency));
		}
	};

	abstract String format(double was, double now, String then1, String then2,String currency);
}